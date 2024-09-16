package com.frankmoley.lil.roomservice.api;

import com.frankmoley.lil.roomservice.data.Room;
import com.frankmoley.lil.roomservice.data.RoomRepository;
import com.frankmoley.lil.roomservice.error.BadReqeustException;
import com.frankmoley.lil.roomservice.error.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final RoomRepository roomRepository;

    public RoomController(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @GetMapping
    public Iterable<Room> getAll() {
        return this.roomRepository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Room addRoom(@RequestBody Room room) {
        return this.roomRepository.save(room);
    }

    @GetMapping("/{id}")
    public Room getRoom(@PathVariable("id") long id) {
        Optional<Room> room = this.roomRepository.findById(id);
        if (room.isEmpty()) {
            throw new NotFoundException("id not found " + id);
        }
        return room.get();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateRoom(@PathVariable("id") long id, @RequestBody Room room) {
        if (id != room.getRoomId()) {
            throw new BadReqeustException("id in body doesn't match path");
        }
        this.roomRepository.save(room);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.RESET_CONTENT)
    public void deleteRoom(@PathVariable("id") long id) {
        this.roomRepository.deleteById(id);
    }
}
