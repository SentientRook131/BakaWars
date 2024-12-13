package com.nanhuboat.Templates;

import com.google.gson.JsonObject;

import java.io.Serializable;

public interface Preservable extends Serializable {
    Preservable read(JsonObject map);
    JsonObject write();
}
