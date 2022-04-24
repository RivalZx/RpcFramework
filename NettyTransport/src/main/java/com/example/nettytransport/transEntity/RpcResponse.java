package com.example.nettytransport.transEntity;

import lombok.*;

/**
 * @program: RPCFramework
 * @description:
 * @author: he-zx
 * @create: 2022-04-21 23:11
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString
public class RpcResponse {
	private String message;
}
