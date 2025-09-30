/*
 * this code is available under GNU GPL v3
 * https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package ru.anotherworld.server.service;

import ru.anotherworld.server.rest.model.SchoolDTO;
import java.util.List;

public interface SchoolService {

    List<SchoolDTO> listAll();

    void delete(Integer id);
    
    SchoolDTO add(Integer number, String name);
    
    SchoolDTO findByNumber(Integer number);

}
