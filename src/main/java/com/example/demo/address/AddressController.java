package com.example.demo.address;

import com.example.demo.common.Response;
import com.example.demo.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user/address")
public class AddressController {
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AddressService addressService;

    @Autowired
    private UserRepository userRepository;

    // api get, add, edit, delete
    @GetMapping("")
    public ResponseEntity<?> getAddress(@RequestParam Long userid){
        if (userRepository.findUsersById(userid)==null){
            return Response.response(null, 404, "Not found user");
        }
        List<Address> list = addressRepository.getAddressByUserId(userid);
        if(list==null)
            return Response.response(null, 200, "No address");
        return Response.response(list, 200, "Success");
    }

    @PostMapping(value = "add")
    public ResponseEntity<?> addAddress(@RequestBody  AddressDto addressDto, @RequestParam Long userid){
        Address address = addressService.addAddress(addressDto, userid);
        return ResponseEntity.ok(addressRepository.findAddressById(address.getId()));
    }

    @PutMapping(value = "update/{id}")
    public ResponseEntity<?> updateAddress(@PathVariable Long id, @RequestBody AddressDto addressDto){
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
