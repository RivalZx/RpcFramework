package com.example.nettytransport.transEntity;

import lombok.*;

/**
 * @program: RPCFramework
 * @description:
 * @author: he-zx
 * @create: 2022-04-21 23:10
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString
public class RpcRequestEntity {
	private String interfaceName;
	private String methodName;
}
