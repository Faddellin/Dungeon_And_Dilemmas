package org.DAD.application.seeding;

import org.DAD.application.repository.GroupRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DBSeeder implements CommandLineRunner {

    private final GroupRepository _groupRepository;

    public DBSeeder(final GroupRepository groupRepository) {
        _groupRepository = groupRepository;
    }

    public void run(final String[] args) {

        _groupRepository.deleteAll();

    }

}
