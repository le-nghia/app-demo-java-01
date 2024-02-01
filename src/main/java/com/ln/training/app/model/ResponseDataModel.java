package com.ln.training.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
@AllArgsConstructor
public class ResponseDataModel {
    private int responseCode;
    private String responseMsg;
    private Object data;

    public ResponseDataModel() {
    }

    public ResponseDataModel(int responseCode, String responseMsg) {
        this.responseCode = responseCode;
        this.responseMsg = responseMsg;
    }
}
