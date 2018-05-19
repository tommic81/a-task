package com.me.poc.domain.location;

import java.io.Serializable;

import static com.me.poc.domain.location.LocationStatus.UNEXPLORED;

public abstract class Location implements Serializable {

    private final String name;
    private final String description;
    private final LocationStatus status;

    public Location(String name, String description, LocationStatus status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocationStatus getStatus() {
        return status;
    }

    public Location(LocationBuilder locationBuilder) {
        this.name = locationBuilder.name;
        this.description = locationBuilder.description;
        this.status = locationBuilder.status;
    }


    public static Location ofLocationType(LocationType locationType) {
        switch (locationType) {
            case TOWN:
                return new Town();
            case GRASSLAND:
                return new Grassland();
            case SETTLEMENT:
                return new Settlement();
            case START:
                return new Start();
            case WOODS:
                return new Woods();
            case WETLANDS:
                return new Wetlands();
        }
        throw new RuntimeException("Cannot create a Location");
    }

    public static LocationBuilder builder() {
        return new LocationBuilder();
    }

    public static class LocationBuilder {
        private LocationType type;
        private String name;
        private String description;
        private LocationStatus status = UNEXPLORED;


        public LocationBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public LocationBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public LocationBuilder withLocationStatus(LocationStatus status) {
            this.status = status;
            return this;
        }

        public LocationBuilder withType(LocationType type) {
            this.type = type;
            return this;
        }

        public Location build() {

            switch (type) {
                case TOWN:
                    return new Town(this);
                case GRASSLAND:
                    return new Grassland(this);
                case SETTLEMENT:
                    return new Settlement(this);
                case START:
                    return new Start(this);
                case WOODS:
                    return new Woods(this);
                case WETLANDS:
                    return new Wetlands(this);
            }
            throw new RuntimeException("Cannot create a Location");
        }


        public void clear() {
            LocationType type = null;
            String name = "";
            String description = "";
            LocationStatus status = UNEXPLORED;
        }
    }

}
