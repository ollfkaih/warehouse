module warehouse.server {
  requires jakarta.ws.rs;

  requires jersey.common;
  requires jersey.server;
  requires jersey.media.json.jackson;

  requires com.fasterxml.jackson.databind;
  requires com.fasterxml.jackson.core;

  requires org.glassfish.hk2.api;
  requires org.slf4j;

  requires warehouse.core;
  requires warehouse.data;
  
  opens restapi to jersey.server;
}
