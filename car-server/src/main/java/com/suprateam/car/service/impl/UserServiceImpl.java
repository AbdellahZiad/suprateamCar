package com.suprateam.car.service.impl;


import com.suprateam.car.dto.UserDto;
import com.suprateam.car.exception.APIException;
import com.suprateam.car.model.Company;
import com.suprateam.car.model.Role;
import com.suprateam.car.model.SmeUser;
import com.suprateam.car.repository.CompanyRepository;
import com.suprateam.car.repository.RoleRepository;
import com.suprateam.car.repository.UserRepository;
import com.suprateam.car.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {


    UserRepository userRepository;

    CompanyRepository companyRepository;

    RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(RoleRepository roleRepository, UserRepository userRepository, CompanyRepository companyRepository) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<UserDto> getAllUser() {
        List<SmeUser> userList = userRepository.findAll().stream().filter(Objects::nonNull).filter(SmeUser::isActive).collect(Collectors.toList());
        return formatUserDto(userList);
    }

    private List<UserDto> formatUserDto(List<SmeUser> userList) {
        List<UserDto> userDtoList = new ArrayList<>();
        for (SmeUser smeUser : userList) {
            UserDto userDto = UserDto.builder()
                    .id(smeUser.getId())
                    .fullName(smeUser.getFullName())
                    .createDate(smeUser.getCreateDate())
                    .validUntil(smeUser.getValidUntil())
                    .active(smeUser.isActive())
//                    .role(smeUser.getRole())
                    .email(smeUser.getEmail())
                    .pw(smeUser.getPw())
                    .checkPassword(smeUser.getPw())
                    .companyName(smeUser.getCompany() != null ? smeUser.getCompany().getName() : null).build();

            userDtoList.add(userDto);

        }

        return userDtoList;
    }

    @Override
    @Transactional
    public String deleteUser(Long id) {
        userRepository.desactivateUserById(id);

        return "Ok";
    }

    @Override
    public UserDto saveUser(UserDto userDto) {

        if (userRepository.existsByEmailIgnoreCase(userDto.getEmail()))
            if (userRepository.findByEmailIgnoreCase(userDto.getEmail()).isActive())
                throw new APIException("Email : " + userDto.getEmail() + " is already exists");

        SmeUser smeUser = new SmeUser();
        Company company = new Company();
        company.setName(userDto.getCompanyName());

        smeUser.setId(userDto.getId());
        smeUser.setEmail(userDto.getEmail());
        smeUser.setFullName(userDto.getFullName());
        smeUser.setCreateDate(userDto.getCreateDate());
        smeUser.setValidUntil(userDto.getValidUntil());
        smeUser.setActive(userDto.isActive());
        smeUser.setPw(userDto.getPw());

        if (companyRepository.findFirstByNameIgnoreCase(userDto.getCompanyName()).isPresent())
            smeUser.setCompany(companyRepository.findFirstByNameIgnoreCase(userDto.getCompanyName()).get());
        else
            smeUser.setCompany(companyRepository.save(company));


//        if (userDto.getRole().toLowerCase().contains("adm"))
//            smeUser.setRoles(Collections.singleton(roleRepository.save(new Role("ROLE_ADMIN","ROLE_ADMIN"))));
//        else
//            smeUser.setRoles(Collections.singleton(roleRepository.save(new Role("ROLE_USER","ROLE_USER"))));

        smeUser.setCreateDate(new Date());
        smeUser.setActive(true);
        smeUser = userRepository.save(smeUser);
        return formatUserDtoFromSME(smeUser);


    }

    private UserDto formatUserDtoFromSME(SmeUser smeUser) {

        return UserDto.builder()
                .id(smeUser.getId())
                .fullName(smeUser.getFullName())
                .email(smeUser.getEmail())
                .companyName(smeUser.getCompany().getName())
                .active(smeUser.isActive())
//                .role(smeUser.getRole())
                .createDate(smeUser.getCreateDate())
                .validUntil(smeUser.getValidUntil())
                .build();
    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        SmeUser user = userRepository.findByEmailIgnoreCase(username);
//        if (user == null) {
//            throw new UsernameNotFoundException("Invalid username or password.");
//        }
//        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPw(), getAuthority(user));
//    }

//    private Set<SimpleGrantedAuthority> getAuthority(SmeUser user) {
//        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
//        user.getRoles().forEach(role -> {
//            //authorities.add(new SimpleGrantedAuthority(role.getName()));
//            authorities.add(new SimpleGrantedAuthority(role.getName()));
//        });
//        return authorities;
//        //return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
//    }
}
