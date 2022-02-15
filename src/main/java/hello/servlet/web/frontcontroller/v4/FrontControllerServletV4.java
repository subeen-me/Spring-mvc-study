package hello.servlet.web.frontcontroller.v4;

import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v4.controller.MemberFormControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberListControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberSaveControllerV4;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "frontControllerServletV4", urlPatterns = "/front-controller/v4/*")
public class FrontControllerServletV4 extends HttpServlet {

    private Map<String, ControllerV4> controllerMap = new HashMap<>();

    public FrontControllerServletV4() { // 서블릿이 생성이 될 때 맵으로 값을 넣어두게 된다.
        controllerMap.put("/front-controller/v4/members/new-form", new MemberFormControllerV4());
        controllerMap.put("/front-controller/v4/members/save", new MemberSaveControllerV4());
        controllerMap.put("/front-controller/v4/members", new MemberListControllerV4());

    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // /front-controller/v4/members key를 넣으면 MemberListControllerV1 이 value로 호출이 된다
        String requestURI = request.getRequestURI();

        //ControllerV3 controller = new MemberListControllerV3(); 과 같음
        ControllerV4 controller = controllerMap.get(requestURI);
        if (controller == null) { // 없으면 404 페이지
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Map<String, String> paramMap = createParamMap(request);
        Map<String, Object> model = new HashMap<>(); // 추가

        String viewName = controller.process(paramMap, model);

        MyView view = viewResolver(viewName);
        view.render(model, request, response);
    }

    // 메소드로 뽑아서 계층을 맞추었다.
    private MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }

    // 단순로직이 아니라 로직이 큰 경우는 메소드로 뽑는다.
    // HttpServletRequest에서 파라미터 정보를 꺼내서 Map으로 변환한다. 그리고 해당 Map( paramMap )을 컨트롤러에 전달하면서 호출한다.
    private Map<String, String> createParamMap(HttpServletRequest request) {
        //paramMap 사용
        Map<String, String> paramMap = new HashMap<>(); //paramMap 을 만든다
        request.getParameterNames().asIterator() //getParameterNames로 모든 파라미터를 다 가져온다.
                // 돌리면서 paramName을 키로, value를 request.getParameter(paramName)으로 다 꺼내온걸 paramMap에 put으로 집어넣는다.
                .forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));
        return paramMap;
    }
}
