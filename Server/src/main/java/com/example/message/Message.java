package com.example.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: RPCFramework
 * @description:
 * @author: he-zx
 * @create: 2022-04-21 21:59
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Serializable {
	private String content;
}
