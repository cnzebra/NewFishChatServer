package org.miaohong.newfishchatserver.core.rpc.server;

import com.google.common.eventbus.Subscribe;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.miaohong.newfishchatserver.core.execption.ServerCoreException;
import org.miaohong.newfishchatserver.core.metric.Counter;
import org.miaohong.newfishchatserver.core.metric.MetricGroup;
import org.miaohong.newfishchatserver.core.metric.SimpleCounter;
import org.miaohong.newfishchatserver.core.rpc.RpcContext;
import org.miaohong.newfishchatserver.core.rpc.RpcHandler;
import org.miaohong.newfishchatserver.core.rpc.eventbus.ServiceRegistedEvent;
import org.miaohong.newfishchatserver.core.rpc.proto.RpcRequest;
import org.miaohong.newfishchatserver.core.rpc.proto.RpcResponse;
import org.miaohong.newfishchatserver.core.rpc.server.proxy.CglibProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> implements RpcHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RpcServerHandler.class);
    private static final Map<String, Object> SERVICE_MAP = new ConcurrentHashMap<>();
    private Counter recordRequestNum;
    private MetricGroup serverMetricGroup;

    public RpcServerHandler(MetricGroup serverMetricGroup) {
        LOG.info("enter RpcServerHandler");
        this.serverMetricGroup = serverMetricGroup;
        if (this.recordRequestNum == null) {
            this.recordRequestNum = new SimpleCounter();
        }
        //FIXME
        this.serverMetricGroup.counter("record-request-num", this.recordRequestNum);
    }


    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        this.recordRequestNum.inc();

    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
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
        String interfaceId = request.getInterfaceId();
        Object serviceBean = SERVICE_MAP.get(interfaceId);
        if (serviceBean == null) {
            throw new ServerCoreException("serviceBean is null");
        }
        LOG.info("serviceBean: {}", serviceBean);
        Class<?> serviceClass = serviceBean.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();

        LOG.info(serviceClass.getName());
        LOG.info(methodName);

        return CglibProxy.invoke(serviceClass, methodName, parameterTypes, serviceBean, parameters);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOG.error("server caught exception", cause);
        ctx.close();
    }

    public static class RpcServerHandlerListener {
        @Subscribe
        public void doAction(final Object event) {
            LOG.info("Received event [{}] and will take a action", event);

            if (event instanceof ServiceRegistedEvent) {
                ServiceRegistedEvent serviceRegistedEvent = (ServiceRegistedEvent) event;
                SERVICE_MAP.put(serviceRegistedEvent.getInterfaceId(), serviceRegistedEvent.getRef());
            }
        }
    }
}
