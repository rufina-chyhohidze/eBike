output "public_ip_address" {
  value = azurerm_public_ip.pip.ip_address
}

output "postgres_admin_user" {
  value = var.postgres_admin_user
}

output "postgresql_fqdn" {
  description = "PostgreSQL Flexible Server Fully Qualified Domain Name"
  value       = azurerm_postgresql_flexible_server.db.fqdn
}

output "postgresql_jdbc_url" {
  description = "JDBC URL for PostgreSQL"
  value       = "jdbc:postgresql://${azurerm_postgresql_flexible_server.db.fqdn}:5432/postgres"
}
