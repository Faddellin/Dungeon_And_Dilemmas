package org.DAD.application.model.CommonModels;

import lombok.*;

import java.util.Dictionary;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseModel {
    private Integer statusCode;
    private Dictionary<String, String> errors;
}