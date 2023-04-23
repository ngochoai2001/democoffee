package com.example.demo.address;

import com.example.demo.user.model.SaveAccount;
import com.example.demo.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService{

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;
    @Override
    public Address addAddress(AddressDto addressDto, Long userid) {
        Address address = new Address();
        address.setAddress(addressDto.getAddress());
        address.setUser(userRepository.findUsersById(userid));
        return addressRepository.save(address);

    }

    @Override
    public Address updateAddress(AddressDto addressDto, Long userid) {
        try{
            Address address = addressRepository.findAddressById(userid);
            address.setAddress(addressDto.getAddress());
            return  addressRepository.save(address);
        }catch (Exception e){
            System.out.println(e);
            return null;
        }

    }


}
