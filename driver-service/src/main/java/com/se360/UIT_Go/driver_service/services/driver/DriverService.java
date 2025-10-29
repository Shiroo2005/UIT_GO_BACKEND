package com.se360.UIT_Go.driver_service.services.driver;

import com.se360.UIT_Go.driver_service.constants.DriverStatus;
import com.se360.UIT_Go.driver_service.constants.RedisKey;
import com.se360.UIT_Go.driver_service.dto.DriverLocationRequest;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.domain.geo.GeoReference;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.se360.UIT_Go.driver_service.constants.RedisKey.KEY_GEO_LOCATIONS;

// @RequiredArgsConstructor // Xóa đi để dùng constructor tùy chỉnh
@Service
public class DriverService implements IDriverService {

    private final RedisTemplate<String, String> redisTemplate;
    private final GeoOperations<String, String> geoOperations;

    @Value("${app.driver-search-max-count:10}")
    private int maxDriverCount;

    public DriverService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.geoOperations = redisTemplate.opsForGeo();
    }

    //------------------------------- UPDATE DRIVER'S LOCATION ---------------------------------------

    @Override
    public void updateLocation(@NonNull String driverId, DriverLocationRequest request) {
        String driverLocationKey = KEY_GEO_LOCATIONS;

        //UPDATE DRIVER STATUS
        this.updateDriverStatus(driverId, request.getStatus());

        //DRIVER AVAILABLE => ADD driver to search set
        if (request.getStatus().equals(DriverStatus.AVAILABLE)) {
            geoOperations.add( // Dùng geoOperations
                    driverLocationKey,
                    new Point(request.getLng(), request.getLat()),
                    driverId
            );
        }
        //ON TRIP
        else {
            geoOperations.remove(driverLocationKey, driverId);
            // ---------------------------------------------
        }
    }

    public void updateDriverStatus(String driverId, @NonNull DriverStatus driverStatus) {
        String driverStatusKey = RedisKey.KEY_DRIVER_STATUS_PREFIX + driverId;

        //UPDATE STATUS
        Map<String, String> driverStatusMap = new HashMap<>();
        driverStatusMap.put("status", driverStatus.toString());
        driverStatusMap.put("last_updated", Instant.now().toString());

        redisTemplate.opsForHash().putAll(driverStatusKey, driverStatusMap);
    }

    @Override
    public String[] searchDriversNear(double lat, double lng, double radiusKm) {

        Point userLocation = new Point(lng, lat);
        Distance radius = new Distance(radiusKm, Metrics.KILOMETERS);

        RedisGeoCommands.GeoSearchCommandArgs args = RedisGeoCommands.GeoSearchCommandArgs
                .newGeoSearchArgs()
                .sortAscending()      // Sắp xếp từ gần đến xa
                .limit(maxDriverCount); // Dùng biến cấu hình

        GeoResults<RedisGeoCommands.GeoLocation<String>> geoResults = geoOperations.search(
                KEY_GEO_LOCATIONS,
                GeoReference.fromCoordinate(userLocation), // Dùng GeoReference.fromPoint
                radius,                               // Truyền trực tiếp object Distance
                args                                  // Truyền args
        );
        // ----------------------------------------------------

        // 2. Chuyển đổi kết quả (chỉ lấy driverId)
        if (geoResults == null) {
            return new String[0];
        }

        // Lấy List<GeoResult> từ GeoResults
        List<GeoResult<RedisGeoCommands.GeoLocation<String>>> resultsList = geoResults.getContent();

        return resultsList
                .stream() // Dùng stream()
                .map(result -> result.getContent().getName())
                .toArray(String[]::new);
    }

    public void updateStatusDriverAcceptTrip(String driverId) {
        this.updateDriverStatus(driverId, DriverStatus.ON_TRIP);
    }

    public void updateStatusDriverOffTrip(String driverId) {
        this.updateDriverStatus(driverId, DriverStatus.AVAILABLE);

    }
}

