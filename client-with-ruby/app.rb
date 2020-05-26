require 'net/https'
require "json"
require 'webrick'
include WEBrick

MY_DAPR_PORT=ARGV[0]

n = 0
s = WEBrick::HTTPServer.new(
    :Port => 1080,
    :HTTPVersion => WEBrick::HTTPVersion.new('1.1')
)
s.mount_proc('/add') do |req, res|
    n += 1
    req_headers =  req.raw_header.map{|x| x.split(/: /).map{|s| s.strip} }.to_h
    traceparent = req_headers["Traceparent"]
    
    p "orderId: #{n}, traceparent-id: #{traceparent}"

    params = {data: {orderId: n}}
    url = "http://localhost:#{MY_DAPR_PORT}/v1.0/invoke/javaapp/method/neworder"
    uri = URI.parse(url)
    http = Net::HTTP.new(uri.host, uri.port)

    headers = { 
        "Content-Type" => "application/json",
        "Traceparent" => traceparent
    }
    response = http.post(uri.path, params.to_json, headers)


    url = "http://localhost:#{MY_DAPR_PORT}/v1.0/invoke/javaapp/method/order"
    uri = URI.parse(url)
    http = Net::HTTP.new(uri.host, uri.port)

    headers = { 
        "Content-Type" => "application/json",
        "Traceparent" => traceparent
    }
    response = http.get(uri.path, headers)


    res.status = 200
    res['Content-Type'] = 'application/json'
    res.body = response.body
end

Signal.trap('INT'){s.shutdown}
s.start