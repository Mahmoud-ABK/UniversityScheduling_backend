#!/bin/bash

# Check if the environment variable is set
if [ -z "$srcprj" ]; then
  echo "The environment variable srcprj is not set."
  exit 1
fi

# Check if at least one parameter is provided
if [ "$#" -lt 1 ]; then
  echo "Usage: $0 <parameter1> <parameter2> ... <parameterN>"
  exit 1
fi

# Loop through each parameter
for x in "$@"; do
  if [ "$x" == "repo" ]; then
    # Create the output file path for repositories
    output_file=$ing1/projet-web-mobile/UniversitySchedule_backend/prompts/forChat/jparepo.txt

    # Write the note and code to the output file
    cat <<EOL > $output_file
**Note**: all of these repositories extend JPARepository
**Note**: a JPARepository has by default these basic CRUD functions:
\`\`\`java
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
List<T> findAll(Sort sort);
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
\`\`\`
Here are JPA repositories:
EOL

    # Append the contents from the repository directory
    cat "$srcprj/repository/"* >> "$output_file"

    echo "Content from repository/* has been written to prompts/forChat/jparepo.txt"

 elif [ "$x" == "impl" ]; then
    # Create the output file path for implementations
    output_file=$ing1/projet-web-mobile/UniversitySchedule_backend/prompts/forChat/impl.txt

    # Concatenate the content from service/impl to the output file
    cat $srcprj/service/impl/* > $output_file

    echo "Content from service/impl/* has been written to prompts/forChat/impl.txt"


  else
    # Create the output file path for other parameters
    output_file="$ing1/projet-web-mobile/UniversitySchedule_backend/prompts/forChat/$x.txt"

    # Concatenate the content for other parameters
    cat $srcprj/$x/* > $output_file
    echo "Content from $x/* has been written to prompts/forChat/$x.txt"
  fi
done
