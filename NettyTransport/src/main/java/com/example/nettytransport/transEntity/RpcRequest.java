package com.example.nettytransport.transEntity;

import lombok.*;

/**
 * @program: RPCFramework
 * @description:
 * @author: he-zx
 * @create: 2022-04-22 14:51
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString
public class RpcRequest {
	private String interfaceName;
	private String methodName;
}
