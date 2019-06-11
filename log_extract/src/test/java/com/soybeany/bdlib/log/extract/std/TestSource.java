package com.soybeany.bdlib.log.extract.std;

/**
 * <br>Created by Soybeany on 2019/6/3.
 */
public class TestSource {

    public static final String SOURCE1 = "[19-06-03 09:01:29] [INFO] [http-nio-8080-exec-8] {RequestListener:33}-FLAG-客户端-开始:188409 /efb/api/v1/flightDispatch/queryTaskList.do {}\n" +
            "[19-06-03 09:01:29] [INFO] [http-nio-8080-exec-7] {RequestListener:33}-FLAG-客户端-开始:188409 /efb/dmsData/getManualList.do {}\n" +
            "[19-06-03 09:01:29] [INFO] [http-nio-8080-exec-9] {RequestListener:33}-FLAG-客户端-开始:188409 /efb/api/v1/ebArticle/queryNew.do {}\n" +
            "[19-06-03 09:01:29] [INFO] [http-nio-8080-exec-10] {RequestListener:33}-FLAG-客户端-开始:188409 /efb/dmsData/getEqpCdList.do {}\n" +
            "[19-06-03 09:01:29] [INFO] [http-nio-8080-exec-8] {FlightDispatchApplicationImpl:117}-staffNum=188409 , startDt=201905270000+0800 , endDt=201906052359+0800 , base=CAN\n" +
            "[19-06-03 09:01:29] [INFO] [http-nio-8080-exec-6] {DevicePropertyInterInfoController:65}-devicePropertyInterAdd>find accessToken[722056@8Ze9b8ZQ08bSzLYKD4JS102F376Poa4p] pilotInfoVo=com.csair.efb.infra.vo.core.PilotInfoVo@e09a\n" +
            "[19-06-03 09:01:29] [INFO] [http-nio-8080-exec-6] {DevicePropertyInterInfoController:67}-devicePropertyInterAdd>find pilotInfoVo=com.csair.efb.infra.vo.core.PilotInfoVo@e09a pilotInfoVo.getDeviceId=76224964\n" +
            "[19-06-03 09:01:29] [INFO] [http-nio-8080-exec-9] {EbApplicationImpl:216}-风险提示调用接口，查询请求参数为：{\"startDt\":\"2019-03-04\",\"endDt\":\"2019-06-04\",\"pageSize\":\"15\",\"dept\":\"190\",\"page\":\"1\",\"newsType\":\"1\",\"base\":\"CAN\"}\n" +
            "[19-06-03 09:01:29] [INFO] [http-nio-8080-exec-9] {DataFromFdpt:33}-EB安全风险接口(getNewEbArtcleInfo)调用URL:172.27.76.193:80/rest/PreFlight/showPreFlightNewsList\n" +
            "[19-06-03 09:01:29] [INFO] [http-nio-8080-exec-10] {RequestListener:33}-FLAG-客户端-结束:188409 /efb/dmsData/getEqpCdList.do {}\n" +
            "[19-06-03 09:01:29] [ERROR] [http-nio-8080-exec-9] {EbApplicationImpl:258}-调用风险提示安全接口失败:{\"code\":6,\"desc\":\"禁止访问该接口，账号密码不正确\"}\n" +
            "[19-06-03 09:01:29] [ERROR] [http-nio-8080-exec-9] {EbApplicationImpl:259}-java.lang.NullPointerException\n" +
            "\tat com.csair.efb.application.impl.ebnet.EbApplicationImpl.queryEbArticleNew(EbApplicationImpl.java:229)\n" +
            "\tat sun.reflect.GeneratedMethodAccessor1933.invoke(Unknown Source)\n" +
            "\tat sun.reflect.DelegatingMethodAccessorImpl.invoke(Unknown Source)\n" +
            "\tat java.lang.reflect.Method.invoke(Unknown Source)\n" +
            "\tat org.springframework.aop.support.AopUtils.invokeJoinpointUsingReflection(AopUtils.java:319)\n" +
            "\tat org.springframework.aop.framework.ReflectiveMethodInvocation.invokeJoinpoint(ReflectiveMethodInvocation.java:183)\n" +
            "\tat org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:150)\n" +
            "\tat org.springframework.transaction.interceptor.TransactionInterceptor.invoke(TransactionInterceptor.java:110)\n" +
            "\tat org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:172)\n" +
            "\tat org.springframework.aop.framework.JdkDynamicAopProxy.invoke(JdkDynamicAopProxy.java:202)\n" +
            "\tat com.sun.proxy.$Proxy80.queryEbArticleNew(Unknown Source)\n" +
            "\tat com.csair.efb.web.controller.api.signpass.EbArticleController.queryEbArticleNew(EbArticleController.java:169)\n" +
            "\tat sun.reflect.GeneratedMethodAccessor1928.invoke(Unknown Source)\n" +
            "\tat sun.reflect.DelegatingMethodAccessorImpl.invoke(Unknown Source)\n" +
            "\tat java.lang.reflect.Method.invoke(Unknown Source)\n" +
            "\tat org.springframework.web.method.support.InvocableHandlerMethod.invoke(InvocableHandlerMethod.java:213)\n" +
            "\tat org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:126)\n" +
            "\tat org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:96)\n" +
            "\tat org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:617)\n" +
            "\tat org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:578)\n" +
            "\tat org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:80)\n" +
            "\tat org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:923)\n" +
            "\tat org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:852)\n" +
            "\tat org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:882)\n" +
            "\tat org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:778)\n" +
            "\tat javax.servlet.http.HttpServlet.service(HttpServlet.java:622)\n" +
            "\tat javax.servlet.http.HttpServlet.service(HttpServlet.java:729)\n" +
            "\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:230)\n" +
            "\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:165)\n" +
            "\tat org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:53)\n" +
            "\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:192)\n" +
            "\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:165)\n" +
            "\tat com.alibaba.druid.support.http.WebStatFilter.doFilter(WebStatFilter.java:123)\n" +
            "\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:192)\n" +
            "\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:165)\n" +
            "\tat com.csair.efb.web.controller.utils.GlobalSafetyFilter.doFilter(GlobalSafetyFilter.java:69)\n" +
            "\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:192)\n" +
            "\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:165)\n" +
            "\tat org.springframework.security.web.FilterChainProxy.doFilter(FilterChainProxy.java:162)\n" +
            "\tat org.springframework.web.filter.DelegatingFilterProxy.invokeDelegate(DelegatingFilterProxy.java:346)\n" +
            "\tat org.springframework.web.filter.DelegatingFilterProxy.doFilter(DelegatingFilterProxy.java:259)\n" +
            "\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:192)\n" +
            "\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:165)\n" +
            "\tat com.csair.efb.web.controller.utils.AccessTokenFilter.doFilter(AccessTokenFilter.java:87)\n" +
            "\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:192)\n" +
            "\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:165)\n" +
            "\tat org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:199)\n" +
            "\tat org.apache.catalina.core.StandardContextValve.__invoke(StandardContextValve.java:108)\n" +
            "\tat org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:40002)\n" +
            "\tat org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:472)\n" +
            "\tat org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:140)\n" +
            "\tat org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:79)\n" +
            "\tat org.apache.catalina.valves.AbstractAccessLogValve.invoke(AbstractAccessLogValve.java:620)\n" +
            "\tat org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:87)\n" +
            "\tat org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:349)\n" +
            "\tat org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:783)\n" +
            "\tat org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:66)\n" +
            "\tat org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:745)\n" +
            "\tat org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1455)\n" +
            "\tat org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49)\n" +
            "\tat java.util.concurrent.ThreadPoolExecutor.runWorker(Unknown Source)\n" +
            "\tat java.util.concurrent.ThreadPoolExecutor$Worker.run(Unknown Source)\n" +
            "\tat org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)\n" +
            "\tat java.lang.Thread.run(Unknown Source)\n" +
            "\n" +
            "[19-06-03 09:01:29] [INFO] [http-nio-8080-exec-9] {EbArticleController:174}-客户端调用风险提示接口。pilotNo=188409,startDt=Mon Mar 04 00:00:00 CST 2019,endDt=Tue Jun 04 00:00:00 CST 2019, eqpCd=[E190] 时间耗费 ：287 ms\n" +
            "[19-06-03 09:01:29] [INFO] [http-nio-8080-exec-9] {RequestListener:33}-FLAG-客户端-结束:188409 /efb/api/v1/ebArticle/queryNew.do {}\n" +
            "[19-06-03 09:01:30] [INFO] [http-nio-8080-exec-5] {RequestListener:33}-FLAG-客户端-开始:188409 /efb/api/v1/weather/sweatherquery.do {latitude:[23.370960],longitude:[113.290443]}\n" +
            "[19-06-03 09:01:30] [INFO] [http-nio-8080-exec-5] {WeatherInfoController:194}-场站天气（/sweatherquery）接收到终端传递过来的参数是  arpCd=null--potArea=null--latitude=23.370960--longitude=113.290443--cityName=null\n" +
            "[19-06-03 09:01:30] [INFO] [http-nio-8080-exec-5] {CodeInfoRepositoryImpl:115}-getRecentCodeInfoByMap>printParams param[latitude]=23.370960\n" +
            "[19-06-03 09:01:30] [INFO] [http-nio-8080-exec-5] {CodeInfoRepositoryImpl:115}-getRecentCodeInfoByMap>printParams param[longitude]=113.290443\n" +
            "[19-06-03 09:01:30] [INFO] [http-nio-8080-exec-4] {UserController:1845}-更新使用的工号：722056\n" +
            "[19-06-03 09:01:30] [INFO] [http-nio-8080-exec-3] {RequestListener:33}-FLAG-客户端-开始:188409 /efb/api/v1/releaseData/deleteRecord.do {fpClearanceDownloadRecs:[[\\n  {\\n    \\ofpNr\\ : \\\\\\n  }\\n]]}\n" +
            "[19-06-03 09:01:30] [INFO] [http-nio-8080-exec-3] {ReleaseDataController:248}-ofpNrJson:[\n" +
            "  {\n" +
            "    \"ofpNr\" : \"\"\n" +
            "  }\n" +
            "]\n" +
            "[19-06-03 09:01:30] [INFO] [http-nio-8080-exec-1] {RequestListener:33}-FLAG-客户端-开始:188409 /efb/api/v1/ebArticle/queryNew.do {}\n" +
            "[19-06-03 09:01:30] [INFO] [http-nio-8080-exec-3] {ReleaseDataController:280}-deleteDownloadRecord客户端传入飞行计划序列号-->[],pilotNo->188409\n" +
            "[19-06-03 09:01:30] [INFO] [http-nio-8080-exec-3] {RequestListener:33}-FLAG-客户端-结束:188409 ";

}
