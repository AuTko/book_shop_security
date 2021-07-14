package com.example.demo.controllers;

import com.example.demo.models.Person;
import com.example.demo.models.Role;
import com.example.demo.models.RoleEnum;
import com.example.demo.payload.request.LoginRequest;
import com.example.demo.payload.request.SignupRequest;
import com.example.demo.payload.response.JwtResponse;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.repository.PersonRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.security.jwt.JwtUtils;
import com.example.demo.security.services.PersonDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/ver1/auth/")
public class AuthController {

    AuthenticationManager authenticationManager;
    PersonRepository personRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    JwtUtils jwtUtils;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, PersonRepository personRepository,
                          RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.personRepository = personRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody @Validated LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        PersonDetailsImpl personDetails = (PersonDetailsImpl) authentication.getPrincipal();
        List<String> roles = personDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                personDetails.getId(),
                personDetails.getFirstName(),
                personDetails.getLastName(),
                personDetails.getUsername(),
                roles)); //huy poka znaet

    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody @Validated SignupRequest signUpRequest) {
        if (personRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        /*if (personRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }*/

        // Create new user's account
        Person person = new Person(signUpRequest.getFirstName(),
                signUpRequest.getLastName(),
                signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword()));

        String strRoles = signUpRequest.getRole();
        Role roles;

        if (strRoles == null) {
            roles = roleRepository.findByDescription(RoleEnum.ROLE_Customer.toString())
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
           // roles = CustomerRole;
        } else {
                switch (strRoles) {
                    case "admin":
                        roles = roleRepository.findByDescription(RoleEnum.ROLE_Admin.toString())
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        //roles.add(adminRole);

                        break;
                    case "owner":
                        roles = roleRepository.findByDescription(RoleEnum.ROLE_Owner.toString())
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                       // roles.add(modRole);

                        break;
                    default:
                        roles = roleRepository.findByDescription(RoleEnum.ROLE_Customer.toString())
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                       // roles.add(userRole);
                }
            }

        person.setRole(roles);
        personRepository.save(person);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
