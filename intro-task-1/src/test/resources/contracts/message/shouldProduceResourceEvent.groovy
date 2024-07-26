package contracts.message

import org.springframework.cloud.contract.spec.Contract

//java.lang.IllegalStateException: org.springframework.beans.factory.NoSuchBeanDefinitionException:
// No bean named 'resource' available

//Contract.make {
//    description("Should produce resource-event when resource was created")
//    input {
//        triggeredBy("createResource()")
//    }
//    label("triggerResourceCreatedEvent")
//    outputMessage {
//        sentTo("resource")
//        body("7fd76e07-31a2-401f-a25b-24906e2c4a1d")
//    }
//}
