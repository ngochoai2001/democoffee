package com.example.demo.address;

import com.example.demo.user.model.SaveAccount;

public class AddressServiceImpl implements AddressService{

    private AddressRepository addressRepository;
    @Override
    public Address addAddress(AddressDto addressDto) {
        Address address = new Address();
        address.setAddress(addressDto.getAddress());
        address.setUser(SaveAccount.users);
        return addressRepository.save(address);

    }

    @Override
    public Address updateAddress(AddressDto addressDto, Long id) {
        try{
            Address address = addressRepository.findAddressById(id);
            address.setAddress(addressDto.getAddress());
            return  addressRepository.save(address);
        }catch (Exception e){
            System.out.println(e);
            return null;
        }

    }


}
