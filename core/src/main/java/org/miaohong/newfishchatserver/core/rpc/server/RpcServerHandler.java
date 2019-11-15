package org.miaohong.newfishchatserver.core.rpc.server;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.miaohong.newfishchatserver.core.proto.RpcRequest;
import org.miaohong.newfishchatserver.core.proto.RpcResponse;
import org.miaohong.newfishchatserver.core.rpc.RpcContext;
import org.miaohong.newfishchatserver.core.rpc.RpcHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> implements RpcHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RpcServerHandler.class);

    private IServiceHandler serviceHandler;

    public RpcServerHandler(IServiceHandler serviceHandler) {
        this.serviceHandler = serviceHandler;
    }

    @Override
    public void channelRead0(final ChannelHandlerContext ctx, final RpcRequest request) {
        LOG.info("Receive request {}", request.getRequestId());
        RpcResponse response = new RpcResponse();
        response.setRequestId(request.getRequestId());
        try {
            Object result = handle(request);
            response.setResult(result);
        } catch (Exception e) {
            response.setError(e.toString());
            LOG.error("RPC Server handle request error", e);
        }
        ctx.writeAndFlush(response).addListener((ChannelFutureListener) channelFuture ->
                LOG.info("Send response for request {}", request.getRequestId()));
    }

    private Object handle(RpcRequest request) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        RpcContext.init(request);
        String className = request.getClassName();
        Object serviceBean = serviceHandler.get(className);
        Class<?> serviceClass = serviceBean.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();

        LOG.info(serviceClass.getName());
        LOG.info(methodName);
        for (int i = 0; i < parameterTypes.length; ++i) {
            LOG.debug(parameterTypes[i].getName());
        }
        if (parameters != null) {
            for (int i = 0; i < parameters.length; ++i) {
                LOG.debug(parameters[i].toString());
            }
        }

        // JDK reflect
        Method method = serviceClass.getMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method.invoke(serviceBean, parameters);

        // Cglib reflect
//        FastClass serviceFastClass = FastClass.create(serviceClass);
//        int methodIndex = serviceFastClass.getIndex(methodName, parameterTypes);
//        return serviceFastClass.invoke(methodIndex, serviceBean, parameters);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOG.error("server caught exception", cause);
        ctx.close();
    }
}