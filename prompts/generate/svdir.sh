#!/bin/bash

# Check if the necessary environment variables are set
if [ -z "$srcprj" ]; then
  echo "Error: The environment variable 'srcprj' is not set."
  exit 1
fi

if [ -z "$ing1" ]; then
  echo "Error: The environment variable 'ing1' is not set."
  exit 1
fi

# Define the destination directory and create it if it doesn't exist
dest="$ing1/projet-web-mobile/UniversitySchedule_backend/prompts/forChat"
mkdir -p "$dest"

#################################
# Special Case: repository (JPA)
#################################
if [ -d "$srcprj/repository" ]; then
  output_file="$dest/jparepo.txt"
  cat <<'EOL' > "$output_file"
**Note**: All of these repositories extend JPARepository
**Note**: A JPARepository has by default these basic CRUD functions:

// Save operations
<S extends T> S save(S entity);
<S extends T> List<S> saveAll(Iterable<S> entities);
<S extends T> S saveAndFlush(S entity);
<S extends T> List<S> saveAllAndFlush(Iterable<S> entities);
void flush();

// Retrieve operations
Optional<T> findById(ID id);
List<T> findAll();
List<T> findAll(Sort sort);
List<T> findAllById(Iterable<ID> ids);
Page<T> findAll(Pageable pageable);
T getById(ID id);
boolean existsById(ID id);
long count();

// Delete operations
void deleteById(ID id);
void delete(T entity);
void deleteAllById(Iterable<? extends ID> ids);
void deleteAll(Iterable<? extends T> entities);
void deleteAll();
void deleteInBatch(Iterable<T> entities);
void deleteAllInBatch();
void deleteAllByIdInBatch(Iterable<ID> ids);

// Example and query methods
<S extends T> long count(Example<S> example);
<S extends T> boolean exists(Example<S> example);

Here are JPA repositories:
EOL

  find "$srcprj/repository" -maxdepth 1 -type f -name "*.java" -exec cat {} + >> "$output_file"
  echo "JPA repositories written to $output_file"
fi

#################################
# Recursively Process Other Folders
#################################

find "$srcprj" -type d | while read -r dir; do
  # Skip repository folder (handled already)
  [[ "$dir" == "$srcprj/repository" ]] && continue

  # Skip if no Java files directly in this folder
  java_files=("$dir"/*.java)
  if [ ! -e "${java_files[0]}" ]; then
    continue
  fi

  # Create relative name and safe output filename
  rel_path="${dir#$srcprj/}"
  [[ "$rel_path" == "$srcprj" ]] && rel_path="root"
  safe_name="${rel_path//\//__}.txt"
  output_file="$dest/$safe_name"

  # Concatenate all Java files in this directory (not recursively)
  cat "$dir"/*.java > "$output_file"
  echo "Folder '$rel_path' content written to $output_file"
done
