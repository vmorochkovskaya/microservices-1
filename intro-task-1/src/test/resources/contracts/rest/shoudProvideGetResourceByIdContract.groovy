package contracts.rest

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name "should get resource by id"
    request {
        method 'GET'
        urlPath $(consumer(regex('/resources/' + uuid())))
    }
    response {
        status 404
    }
}