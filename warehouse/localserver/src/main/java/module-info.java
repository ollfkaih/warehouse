open module warehouse.localserver {
  requires transitive warehouse.core;
  requires warehouse.data;
  requires com.fasterxml.jackson.core;

  exports localserver;
}
