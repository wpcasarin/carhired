package org.ifsul.carhired.api.automaker;

import org.ifsul.carhired.api.automaker.dto.AutomakerCreateDTO;
import org.ifsul.carhired.api.automaker.dto.AutomakerDTO;
import org.ifsul.carhired.api.automaker.dto.AutomakerUpdateDTO;
import org.ifsul.carhired.api.model.Model;
import org.ifsul.carhired.api.model.ModelRepository;
import org.ifsul.carhired.api.model.ModelService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(classes = {AutomakerService.class})
class AutomakerServiceTest {
    @MockBean
    private AutomakerRepository automakerRepository;
    @Autowired
    private AutomakerService automakerService;
    @MockBean
    private ModelMapper modelMapper;
    @MockBean
    private ModelRepository modelRepository;
    @MockBean
    private ModelService modelService;

    //CREATE
    @Test
    void testCreateAutomaker() {
        when(automakerRepository.save(Mockito.<Automaker>any())).thenReturn(new Automaker());
        AutomakerDTO automakerDTO = new AutomakerDTO();
        when(modelMapper.map(Mockito.<Automaker>any(), Mockito.<Class<AutomakerDTO>>any())).thenReturn(automakerDTO);

        AutomakerCreateDTO request = new AutomakerCreateDTO();
        request.setCountry("Brazil");
        request.setName("Automaker");
        assertEquals(automakerDTO, automakerService.createAutomaker(request));
        verify(automakerRepository).save(Mockito.<Automaker>any());
        verify(modelMapper).map(Mockito.<Automaker>any(), Mockito.<Class<AutomakerDTO>>any());
    }

    //UPDATE
    @Test
    void testUpdateAutomaker_WhenAutomakerExists() {
        when(automakerRepository.save(Mockito.<Automaker>any())).thenReturn(new Automaker());
        Optional<Automaker> ofResult = Optional.of(new Automaker());
        when(automakerRepository.findByIdAndDeletedIsFalse(Mockito.<Long>any())).thenReturn(ofResult);
        AutomakerDTO automakerDTO = new AutomakerDTO();
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<AutomakerDTO>>any())).thenReturn(automakerDTO);
        doNothing().when(modelMapper).map(Mockito.<Object>any(), Mockito.<Object>any());

        AutomakerUpdateDTO request = new AutomakerUpdateDTO();
        request.setCountry("GB");
        request.setName("Name");
        assertSame(automakerDTO, automakerService.updateAutomaker(1L, request));
        verify(automakerRepository).save(Mockito.<Automaker>any());
        verify(automakerRepository).findByIdAndDeletedIsFalse(Mockito.<Long>any());
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<AutomakerDTO>>any());
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Object>any());
    }

    @Test
    void testUpdateAutomaker_WhenAutomakerNotFound() {
        Optional<Automaker> emptyResult = Optional.empty();
        when(automakerRepository.findByIdAndDeletedIsFalse(Mockito.<Long>any())).thenReturn(emptyResult);
        AutomakerUpdateDTO request = new AutomakerUpdateDTO();
        request.setCountry("Brazil");
        request.setName("Automaker");
        assertThrows(OpenApiResourceNotFoundException.class, () -> automakerService.updateAutomaker(1L, request));
    }

    // DELETE
    @Test
    void testDeleteAutomakerById_Success_NoModels() {
        doNothing().when(automakerRepository).deleteByIdSoftly(Mockito.<Long>any());
        Optional<Automaker> ofResult = Optional.of(new Automaker(1L, "Automaker", "Brazil", new ArrayList<>()));
        when(automakerRepository.findByIdAndDeletedIsFalse(Mockito.<Long>any())).thenReturn(ofResult);
        automakerService.deleteAutomakerById(1L);
        verify(automakerRepository).findByIdAndDeletedIsFalse(Mockito.<Long>any());
        verify(automakerRepository).deleteByIdSoftly(Mockito.<Long>any());
    }

    @Test
    void testDeleteAutomakerById_Success_WithModels() {
        ArrayList<Model> models = new ArrayList<>();
        models.add(new Model());
        models.add(new Model());
        Optional<Automaker> ofResult = Optional.of(new Automaker(1L, "Name", "GB", models));
        doNothing().when(automakerRepository).deleteByIdSoftly(Mockito.<Long>any());
        when(automakerRepository.findByIdAndDeletedIsFalse(Mockito.<Long>any())).thenReturn(ofResult);
        doNothing().when(modelService).deleteModelById(Mockito.<Long>any());
        automakerService.deleteAutomakerById(1L);
        verify(automakerRepository).findByIdAndDeletedIsFalse(Mockito.<Long>any());
        verify(automakerRepository).deleteByIdSoftly(Mockito.<Long>any());
        verify(modelService, atLeast(1)).deleteModelById(Mockito.<Long>any());
    }


    @Test
    void testDeleteAutomakerById_NotFound() {
        Optional<Automaker> emptyResult = Optional.empty();
        when(automakerRepository.findByIdAndDeletedIsFalse(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(OpenApiResourceNotFoundException.class, () -> automakerService.deleteAutomakerById(1L));
        verify(automakerRepository).findByIdAndDeletedIsFalse(Mockito.<Long>any());
    }

    //    READ
    @Test
    void testFindAllAutomakers_WhenAutomakersIsEmpty() {
        when(automakerRepository.findAllByDeletedIsFalse()).thenReturn(new ArrayList<>());
        List<AutomakerDTO> result = automakerService.findAllAutomakers();
        assertTrue(result.isEmpty());
        verify(automakerRepository).findAllByDeletedIsFalse();
        verify(modelMapper, never()).map(any(Automaker.class), eq(AutomakerDTO.class));
    }


    @Test
    void testFindAllAutomakers_WhenAutomakersExists() {
        ArrayList<Automaker> automakers = new ArrayList<>();
        automakers.add(new Automaker());
        automakers.add(new Automaker());
        automakers.add(new Automaker());
        when(automakerRepository.findAllByDeletedIsFalse()).thenReturn(automakers);
        when(modelMapper.map(any(Automaker.class), eq(AutomakerDTO.class)))
                .thenReturn(new AutomakerDTO());
        List<AutomakerDTO> result = automakerService.findAllAutomakers();
        assertEquals(3, result.size());
        verify(automakerRepository).findAllByDeletedIsFalse();
        verify(modelMapper, times(3)).map(any(Automaker.class), eq(AutomakerDTO.class));
        for (AutomakerDTO automakerDTO : result) {
            assertNotNull(automakerDTO);
        }
    }


}

