package fr.duchesses.moov.apis;

import com.google.common.collect.Lists;
import fr.duchesses.moov.models.Coordinates;
import fr.duchesses.moov.models.Transport;
import fr.duchesses.moov.models.TransportType;

import java.util.Collection;
import java.util.List;

public class VelibApiService implements ApiService{
    public Collection<Transport> getVelibs(){
        List<Transport> transports = Lists.newArrayList(new Transport(TransportType.VELIB, new Coordinates(10, 20)),
                new Transport(TransportType.VELIB, new Coordinates(10, 19)),
                new Transport(TransportType.VELIB, new Coordinates(10, 18)),
                new Transport(TransportType.VELIB, new Coordinates(11, 20)));
        return transports;
    }
}
