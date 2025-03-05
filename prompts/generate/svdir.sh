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

#########################
# Special Case: repository
#########################
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

  # Append all files in the repository directory
  cat "$srcprj/repository/"* >> "$output_file"
  echo "Content from repository/* has been written to $output_file"
fi

#########################
# Special Case: service/impl
#########################
if [ -d "$srcprj/service/impl" ]; then
  output_file1="$dest/impl1.txt"
  output_file2="$dest/impl2.txt"

  files=("$srcprj/service/impl/"*) # Get list of files
  total_files=${#files[@]}
  mid=$((total_files / 2)) # Find the midpoint to split

  # Write first half to impl1.txt
  cat "${files[@]:0:mid}" > "$output_file1"

  # Write second half to impl2.txt
  cat "${files[@]:mid}" > "$output_file2"

  echo "Content from service/impl/* has been split into $output_file1 and $output_file2"
fi

#########################################
# Process all other top-level directories
#########################################
for d in "$srcprj"/*/; do
  dir_name=$(basename "$d")

  # Skip directories that were already handled specially
  if [ "$dir_name" = "repository" ]; then
    continue
  fi

  # For the service directory, process only files outside the impl subfolder
  if [ "$dir_name" = "service" ]; then
    output_file="$dest/service.txt"
    # Clear (or create) the output file
    > "$output_file"
    for file in "$srcprj/service/"*; do
      # Skip the impl subdirectory (which was handled above)
      if [ -d "$file" ]; then
        continue
      fi
      cat "$file" >> "$output_file"
    done
    echo "Content from service (excluding impl) has been written to $output_file"
    continue
  fi

  # Process any other directory normally
  output_file="$dest/${dir_name}.txt"
  cat "$d"* > "$output_file"
  echo "Content from ${dir_name}/* has been written to $output_file"
done
