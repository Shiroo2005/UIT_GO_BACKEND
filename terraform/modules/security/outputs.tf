output "sg_public_alb_id" { value = aws_security_group.public_alb.id }
output "sg_internal_alb_id" { value = aws_security_group.internal_alb.id }
output "sg_ecs_services_id" { value = aws_security_group.ecs_services.id }
output "sg_rds_user_id" { value = aws_security_group.rds_user.id }
output "sg_rds_trip_id" { value = aws_security_group.rds_trip.id }
output "sg_redis_driver_id" { value = aws_security_group.redis_driver.id }
output "sg_msk_id" { value = aws_security_group.msk_infra.id }

output "ecs_task_execution_role_arn" { value = aws_iam_role.ecs_task_execution_role.arn }
output "ecs_task_role_arn" { value = aws_iam_role.ecs_task_role.arn }