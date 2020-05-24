require 'net/https'
require "json"

MY_DAPR_PORT=ARGV[0]

n = 0
while true do
    n += 1
    params = {data: {orderId: n}}

    url = "http://localhost:#{MY_DAPR_PORT}/v1.0/invoke/javaapp/method/neworder"
    uri = URI.parse(url)
    http = Net::HTTP.new(uri.host, uri.port)

    headers = { "Content-Type" => "application/json" }
    response = http.post(uri.path, params.to_json, headers)

    sleep 1
end