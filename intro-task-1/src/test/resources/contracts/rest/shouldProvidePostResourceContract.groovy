package contracts.rest

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'POST'
        url '/resources'
        headers {
            contentType('audio/mpeg')
        }
        body(fileAsBytes('sample.mp3'))
    }
    response {
        status 200
        headers {
            contentType(applicationJson())
        }
        body([
                id: $(consumer('d95a40e6-d69b-4c2f-8256-cfcd3f925ff6'), producer(regex('[a-f0-9-]{36}')))
        ])
    }
}

