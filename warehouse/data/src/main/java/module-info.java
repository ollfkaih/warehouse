module warehouse.data {
  requires transitive warehouse.core.main;
  requires com.fasterxml.jackson.core;
  requires transitive com.fasterxml.jackson.databind;

  exports data;
  opens data;
}
