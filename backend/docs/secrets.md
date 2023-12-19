```


cd deploy

age-keygen -o age.agekey


cat age.agekey |
kubectl create secret generic sops-age \
--namespace=kibon-dev \
--from-file=age.agekey=/dev/stdin

sops --age=age1xa5rf0wyew7k2r9378p5n05lt2e9ev0rrqtgh0mlzfkkqzl9qy2q3ljxfl \
--encrypt --encrypted-regex '^(data|stringData)$' --in-place database-secrets.yml





```