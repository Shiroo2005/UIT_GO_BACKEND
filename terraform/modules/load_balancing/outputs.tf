output "public_alb_tg_api_gateway_arn" {
  value = aws_lb_target_group.api_gateway.arn
}
output "internal_alb_tg_eureka_arn" {
  value = aws_lb_target_group.eureka_server.arn
}
output "internal_alb_dns_name" {
  value = aws_lb.internal.dns_name
}