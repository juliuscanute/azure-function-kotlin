output "database_id" {
  value = "${module.cosmos.database_id}"
}

output "read_endpoints" {
  value = "${module.cosmos.read_endpoints}"
}

output "write_endpoints" {
  value = "${module.cosmos.write_endpoints}"
}

output "primary_readonly_master_key" {
  value = "${module.cosmos.primary_readonly_master_key}"
}

output "primary_master_key" {
  value = "${module.cosmos.primary_master_key}"
}

output "connection_strings" {
  value = "${module.cosmos.connection_strings}"
}