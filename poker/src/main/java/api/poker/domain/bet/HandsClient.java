package api.poker.domain.bet;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@FeignClient(name = "handsClient", url = "http://localhost:7070")
public interface HandsClient {
    @PostMapping("/hands")
    ResponseEntity<String> compareHands(@RequestBody List<Map<String, Object>> requestBody);
}
