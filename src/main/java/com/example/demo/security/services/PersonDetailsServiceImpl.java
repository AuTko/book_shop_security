package com.example.demo.security.services;

import com.example.demo.models.Person;
import com.example.demo.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonDetailsServiceImpl implements UserDetailsService {
    @Autowired
    PersonRepository personRepository;

    /*@Autowired
    public PersonDetailsServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }
*/
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       Person person = personRepository.findByEmail(username).
               orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + username));

       return PersonDetailsImpl.build(person);
    }
}
