module warehouse.rest {
  requires warehouse.core;
  requires warehouse.data;

  requires jakarta.ws.rs;

  requires jersey.common;
  requires jersey.server;
  requires jersey.media.json.jackson;

  requires com.fasterxml.jackson.databind;

  requires org.glassfish.hk2.api;
  requires org.slf4j;

  opens restapi to jersey.server;
}
