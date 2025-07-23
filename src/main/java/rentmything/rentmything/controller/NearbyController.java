package rentmything.rentmything.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import rentmything.rentmything.model.Role;
import rentmything.rentmything.model.User;
import rentmything.rentmything.repository.UserRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@RestController
@RequestMapping("/rentMyThing")
public class NearbyController {

    @Autowired
    private UserRepository userRepository;

    // Find owners within 5km radius
    @GetMapping("/findNearbyOwners")
    public ResponseEntity<?> findNearbyOwners(
            @RequestParam BigDecimal latitude,
            @RequestParam BigDecimal longitude) {

        List<User> owners = userRepository.findByRole(Role.OWNER);
        List<Map<String, Object>> nearbyOwners = new ArrayList<>();

        for (User owner : owners) {
            if (owner.getLatitude() != null && owner.getLongitude() != null) {
                BigDecimal dist = haversine(latitude, longitude, owner.getLatitude(), owner.getLongitude());

                // Use compareTo for BigDecimal comparison
                if (dist.compareTo(BigDecimal.valueOf(5.0)) <= 0) {
                    Map<String, Object> info = new HashMap<>();
                    info.put("name", owner.getName());
                    info.put("email", owner.getEmail());

                    // Format distance to 2 decimal places
                    String formatted = dist.setScale(2, RoundingMode.HALF_UP).toPlainString() + " km";
                    info.put("distance", formatted);

                    nearbyOwners.add(info);
                }
            }
        }

        return ResponseEntity.ok(nearbyOwners);
    }

    // Haversine formula using BigDecimal
    private BigDecimal haversine(BigDecimal lat1, BigDecimal lon1, BigDecimal lat2, BigDecimal lon2) {
        final double R = 6371; // Earth's radius in km

        // Convert BigDecimal to double for math
        double dLat = Math.toRadians(lat2.subtract(lat1).doubleValue());
        double dLon = Math.toRadians(lon2.subtract(lon1).doubleValue());

        double lat1Rad = Math.toRadians(lat1.doubleValue());
        double lat2Rad = Math.toRadians(lat2.doubleValue());

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return BigDecimal.valueOf(R * c);
    }
}
