module warehouse.server {
  requires com.fasterxml.jackson.databind;

  requires spring.web;
  requires spring.beans;
  requires spring.boot;
  requires spring.context;
  requires spring.core;
  requires spring.boot.autoconfigure;

  requires warehouse.core;
  requires warehouse.data;

  opens server to spring.beans, spring.context, spring.web, spring.core;
}
