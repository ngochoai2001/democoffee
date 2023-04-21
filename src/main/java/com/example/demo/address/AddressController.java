package com.example.demo.address;

import com.example.demo.user.model.SaveAccount;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user/address")
public class AddressController {

    private AddressRepository addressRepository;
    private AddressService addressService;

    // api get, add, edit, delete
    @GetMapping("")
    public ResponseEntity<?> getAddress(){
//        return ResponseEntity.ok("list address ne");
        List<Address> list = addressRepository.getAddressByUserId(SaveAccount.users.getId());
        if(list==null)
            return ResponseEntity.ok("None");
        return ResponseEntity.ok(addressRepository.getAddressByUserId(SaveAccount.users.getId()));
    }

    @PostMapping(value = "add")
    public ResponseEntity<?> addAddress(@ModelAttribute  AddressDto addressDto){
        Address address = addressService.addAddress(addressDto);
        return ResponseEntity.ok(addressRepository.findAddressById(address.getId()));
    }

    @PutMapping(value = "update/{id}")
    public ResponseEntity<?> updateAddress(@PathVariable Long id, @ModelAttribute AddressDto addressDto){
        Address address = addressService.updateAddress(addressDto, id);
        if (address==null)
            return ResponseEntity.ok("Cannot found");
        return ResponseEntity.ok(addressRepository.findAddressById(address.getId()));
    }

    @DeleteMapping(value = "delete/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable Long id){
        if(addressRepository.findAddressById(id)==null)
            return ResponseEntity.ok("Cannnot found");
        addressRepository.deleteById(id);
        return ResponseEntity.ok("Delete success");
    }
}
