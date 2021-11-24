open module warehouse.localserver {
  requires transitive warehouse.core.main;
  requires warehouse.core.server;
  requires warehouse.core.client;
  requires warehouse.data;
  requires com.fasterxml.jackson.core;

  exports localserver;
}
