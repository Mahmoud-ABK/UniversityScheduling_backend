#!/bin/bash

# Directory containing the Java files
DIRECTORY="/home/mahmoud/Engineering/ing1/projet-web-mobile/UniversitySchedule_backend/src/main/java/com/scheduling/universityschedule_backend/model"

# Check if the directory exists
if [ ! -d "$DIRECTORY" ]; then
    echo "Directory $DIRECTORY does not exist."
    exit 1
fi

# Find all Java files and process them
find "$DIRECTORY" -type f -name "*.java" | while read -r file; do
    echo "Processing $file..."
    
    # Create a temporary file
    temp_file=$(mktemp)
    
    # Use sed to modify @OneToMany and @OneToOne annotations
    sed -E '
        # Handle @OneToMany
        s/(@OneToMany\([^)]*)\)/\1, cascade = CascadeType.ALL , orphanRemoval = true\)/g;
        # Handle @OneToOne with parentheses
        s/(@OneToOne\([^)]*)\)/\1, cascade = CascadeType.ALL , orphanRemoval = true\)/g;
        # Handle @OneToOne without parentheses
        s/(@OneToOne)(\s|$)/\1(cascade = CascadeType.ALL , orphanRemoval = true)\2/g
    ' "$file" > "$temp_file"
    
    # Check if changes were made
    if ! cmp -s "$file" "$temp_file"; then
        mv "$temp_file" "$file"
        echo "Modified $file"
    else
        rm "$temp_file"
        echo "No changes needed for $file"
    fi
done

echo "Processing complete."