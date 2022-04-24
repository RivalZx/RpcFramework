package com.hezx.testmodule.serializable.normal;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

/**
 * @program: RPCFramework
 * @description:
 * @author: he-zx
 * @create: 2022-04-21 16:45
 **/

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RPCRequest implements Serializable {
	private static final long serialVersionId = 239847193472934L; // 版本控制，检查需要序列化的类是不是同一个
	private String requestID; // 请求ID
	private String interfaceName;
	private String methodName;
	private Object[] parameters;
	private Class<?>[] paramTypes;
	private RpcMessageTypeEnum rpcMessageTypeEnum;
}
