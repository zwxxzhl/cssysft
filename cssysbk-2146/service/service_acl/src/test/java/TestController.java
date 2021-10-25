//import com.atguigu.aclservice.enums.RefundReasonEvents;
//import com.atguigu.aclservice.enums.RefundReasonStatus;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.statemachine.StateMachine;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class TestController {
//    @Autowired
//    private StateMachine<RefundReasonStatus, RefundReasonEvents> stateMachine;
//
//
//    @GetMapping("test/machine")
//    public void testMachine() {
//        stateMachine.start();
//        stateMachine.sendEvent(RefundReasonEvents.APPROVE);
//    }
//}
//
