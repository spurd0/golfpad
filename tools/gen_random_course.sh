#!/bin/bash

ASSETS_DIR="../app/src/main/assets"
mkdir -p $ASSETS_DIR
FILE="$ASSETS_DIR/course_data.json"

echo "[" > "$FILE"

for hole_idx in {1..18}
do
    awk -v seed=$RANDOM -v h_num=$hole_idx 'BEGIN {
        srand(seed);
        
        mid_lat = 55.7 + (rand() * 0.1);
        mid_lon = 37.6 + (rand() * 0.1);
        par = (rand() > 0.8) ? 5 : ((rand() < 0.2) ? 3 : 4);
        offset = 0.00018 + (rand() * 0.0001);
        
        printf "  {\n"
        printf "    \"holeNumber\": %d,\n", h_num
        printf "    \"par\": %d,\n", par
        printf "    \"green\": {\n"
        printf "      \"front\":  {\"lat\": %.7f, \"lon\": %.7f},\n", mid_lat - offset, mid_lon
        printf "      \"middle\": {\"lat\": %.7f, \"lon\": %.7f},\n", mid_lat, mid_lon
        printf "      \"back\":   {\"lat\": %.7f, \"lon\": %.7f}\n", mid_lat + offset, mid_lon
        printf "    },\n"
        printf "    \"shots\": [\n"
        
        curr_lat = mid_lat - (0.004 + rand() * 0.001);
        curr_lon = mid_lon - (0.004 + rand() * 0.001);
        
        is_dnf = (rand() < 0.15);
        total_shots = is_dnf ? (1 + int(rand() * 2)) : (2 + int(rand() * 4));
        
        for (i = 1; i <= total_shots; i++) {
            out_lat = curr_lat;
            out_lon = curr_lon;
            
            if (!is_dnf && i == total_shots) {
                out_lat = mid_lat;
                out_lon = mid_lon;
            }

            printf "      {\"lat\": %.7f, \"lon\": %.7f}%s\n", out_lat, out_lon, (i == total_shots ? "" : ",")
            
            dist_lat = mid_lat - curr_lat;
            dist_lon = mid_lon - curr_lon;
            step_ratio = 0.75 + (rand() * 0.15);
            curr_lat += (dist_lat * step_ratio) + ((rand()-0.5) * 0.00003);
            curr_lon += (dist_lon * step_ratio) + ((rand()-0.5) * 0.00003);
        }
        
        printf "    ]\n"
        printf "  }%s\n", (h_num == 18 ? "" : ",")
    }' >> "$FILE"
done

echo "]" >> "$FILE"
