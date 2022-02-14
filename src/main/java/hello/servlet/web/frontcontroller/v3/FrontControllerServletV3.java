package hello.servlet.web.frontcontroller.v3;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v2.ControllerV2;
import hello.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "frontControllerServletV3", urlPatterns = "/front-controller/v3/*")
public class FrontControllerServletV3 extends HttpServlet {

    private Map<String, ControllerV3> controllerMap = new HashMap<>();

    public FrontControllerServletV3() { // 서블릿이 생성이 될 때 맵으로 값을 넣어두게 된다.
        controllerMap.put("/front-controller/v3/members/new-form", new MemberFormControllerV3());
        controllerMap.put("/front-controller/v3/members/save", new MemberSaveControllerV3());
        controllerMap.put("/front-controller/v3/members", new MemberListControllerV3());

    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // /front-controller/v3/members key를 넣으면 MemberListControllerV1 이 value로 호출이 된다
        String requestURI = request.getRequestURI();

        //ControllerV3 controller = new MemberListControllerV1(); 과 같음
        ControllerV3 controller = controllerMap.get(requestURI);
        if (controller == null) { // 없으면 404 페이지
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Map<String, String> paramMap = createParamMap(request);
        ModelView mv = controller.process(paramMap);

        String viewName = mv.getViewName(); //논리이름 new-form
        MyView view = viewResolver(viewName);

        view.render(mv.getModel(), request, response);
    }

    // 메소드로 뽑아서 계층을 맞추었다.
    private MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }

    // 단순로직이 아니라 로직이 큰 경우는 메소드로 뽑는다.
    private Map<String, String> createParamMap(HttpServletRequest request) {
        //paramMap 사용
        Map<String, String> paramMap = new HashMap<>(); //paramMap 을 만든다
        request.getParameterNames().asIterator() //getParameterNames로 모든 파라미터를 다 가져온다.
                // 돌리면서 paramName을 키로, value를 request.getParameter(paramName)으로 다 꺼내온걸 paramMap에 put으로 집어넣는다.
                .forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));
        return paramMap;
    }
}
