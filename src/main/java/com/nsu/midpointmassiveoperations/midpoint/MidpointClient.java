package com.nsu.midpointmassiveoperations.midpoint;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "midpoint", url = "${midpoint.base-url}", configuration = MidpointClientConfig.class)
public interface MidpointClient {

    @GetMapping("/users/mail/{email}")
    void searchUserByEmail(@PathVariable("email") String email);
}
