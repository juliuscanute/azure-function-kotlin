provider "azurerm" {
  version = "=1.28.0"
}

terraform {
  backend "azurerm" {}
}

data "terraform_remote_state" "init" {
  backend = "azurerm"
  config = {
    storage_account_name = "${var.storage_account_name}"
    container_name = "${var.container_name}"
    key = "${var.key}"
    access_key = "${var.access_key}"
  }
}

data "azurerm_storage_account" "storage_account" {
  name                = "${var.storage_account_name}"
  resource_group_name = "${data.terraform_remote_state.init.resource_group_name}"
}


module "cosmos" {
  source = "git@github.com:juliuscanute/azure-terraform-modules.git//serverless-storage?ref=0.0.17"
  resource_group_name = "${data.terraform_remote_state.init.resource_group_name}"
  resouce_group_location = "${data.terraform_remote_state.init.resource_group_location}"
  cosmos_db_name = "${var.cosmos_db_name}"
  failover_location = "${var.failover_location}"
}

module "function_app" {
  source = "git@github.com:juliuscanute/azure-terraform-modules.git//serverless-functions?ref=0.0.22"
  resource_group_name = "${data.terraform_remote_state.init.resource_group_name}"
  resouce_group_location = "${data.terraform_remote_state.init.resource_group_location}"
  function_app_name = "dictionary-20190626184020334"
  storage_connection_string = "${data.azurerm_storage_account.storage_account.primary_connection_string}"
  host_name ="${element(module.cosmos.read_endpoints,0)}"
  master_key = "${module.cosmos.primary_readonly_master_key}"
  database_uri = "${var.database_uri}"
  collection_uri = "${var.collection_uri}"
}