//package com.suprateam.car.service.impl;
//
//
//import com.suprateam.car.dto.UserDto;
//import com.suprateam.car.model.Company;
//import com.suprateam.car.model.Role;
//import com.suprateam.car.model.SmeUser;
//import com.suprateam.car.repository.CompanyRepository;
//import com.suprateam.car.repository.RoleRepository;
//import com.suprateam.car.repository.UserRepository;
//import com.suprateam.car.service.UserServiceAuth;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import javax.transaction.Transactional;
//import java.util.*;
//import java.util.stream.Collectors;
//
//
//@Service(value = "userService")
//public class UserServiceAuthImpl implements UserDetailsService, UserServiceAuth {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private RoleRepository roleRepository;
//
//    @Autowired
//    private BCryptPasswordEncoder bcryptEncoder;
//
//    @Autowired
//    CompanyRepository companyRepository;
//
//    @Transactional
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        SmeUser user = userRepository.findByEmailIgnoreCase(email);
//        if (user == null) {
//            throw new UsernameNotFoundException("Invalid username or password.");
//        }
////        if (user.getRole() != null && isAgent(user.getRole().getRole()))
////            throw new UsernameNotFoundException("Agents can't access to User interface, please contact administration  for any information or question");
//
//        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPw(), getAuthority(user));
//    }
//
//    private boolean isAgent(String role) {
//        return role != null && role.toLowerCase().contains("agent");
//    }
//
//    private Set<SimpleGrantedAuthority> getAuthority(SmeUser user) {
//        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
////        user.getRoles().forEach(role -> {
////            //authorities.add(new SimpleGrantedAuthority(role.getName()));
////            authorities.add(new SimpleGrantedAuthority(role.getName()));
////        });
//        authorities.add(new SimpleGrantedAuthority(user.getRole().getRole()));
//        return authorities;
//        //return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
//    }
//
//
//    public List<SmeUser> findAll() {
//        List<SmeUser> list = new ArrayList<>();
//        userRepository.findAll().iterator().forEachRemaining(list::add);
//        return list;
//    }
//
//    @Override
//    public void delete(long id) {
//        userRepository.deleteById(id);
//    }
//
//    @Override
//    public SmeUser findOne(String username) {
//        return userRepository.findByEmailIgnoreCase(username);
//    }
//
//    @Override
//    public SmeUser findById(Long id) {
//        return userRepository.findById(id).get();
//    }
//
//    @Override
//    public SmeUser save(UserDto user) {
//
//        SmeUser newUser = new SmeUser();
//
//        Company company = new Company();
//        company.setName(user.getCompanyName());
//        newUser.setEmail(user.getEmail());
//        newUser.setPw(bcryptEncoder.encode(user.getPw()));
//        newUser.setCreateDate(new Date());
//        newUser.setActive(true);
//        newUser.setValidUntil(user.getValidUntil());
//        newUser.setFullName(user.getFullName());
//
//        if (companyRepository.findFirstByNameIgnoreCase(user.getCompanyName()).isPresent())
//            newUser.setCompany(companyRepository.findFirstByNameIgnoreCase(user.getCompanyName()).get());
//        else
//            newUser.setCompany(companyRepository.save(company));
//
//        if (user.getRole().toLowerCase().contains("admin"))
//            newUser.setRole(roleRepository.save(new Role("ADMIN", "ADMIN")));
//        else if (user.getRole().toLowerCase().contains("agent"))
//            newUser.setRole(roleRepository.save(new Role("AGENT", "AGENT")));
//        else if (user.getRole().toLowerCase().contains("underwriter"))
//            newUser.setRole(roleRepository.save(new Role("UNDERWRITER", "UNDERWRITER")));
//
//
//        return userRepository.save(newUser);
//    }
//
//
//    @Override
//    public List<UserDto> getAllUser() {
//        List<SmeUser> userList = userRepository.findAll().stream().filter(Objects::nonNull).filter(SmeUser::isActive)
//                .collect(Collectors.toList());
//        return formatUserDto(userList);
//    }
//
//    private List<UserDto> formatUserDto(List<SmeUser> userList) {
//        List<UserDto> userDtoList = new ArrayList<>();
//        for (SmeUser smeUser : userList) {
//            UserDto userDto = UserDto.builder()
//                    .id(smeUser.getId())
//                    .fullName(smeUser.getFullName())
//                    .createDate(smeUser.getCreateDate())
//                    .validUntil(smeUser.getValidUntil())
//                    .active(smeUser.isActive())
//                    .role(smeUser.getRole()!=null?smeUser.getRole().getRole():"")
//                    .email(smeUser.getEmail())
//                    .pw(smeUser.getPw())
//                    .checkPassword(smeUser.getPw())
//                    .companyName(smeUser.getCompany() != null ? smeUser.getCompany().getName() : null).build();
//
//            userDtoList.add(userDto);
//
//        }
//
//        return userDtoList;
//    }
//
//    @Override
//    @Transactional
//    public String deleteUser(Long id) {
//        userRepository.desactivateUserById(id);
//
//        return "Ok";
//    }
//}
