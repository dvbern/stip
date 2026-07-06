#let data = json(bytes(sys.inputs.at("data")))

#set page(margin: (x: 2.5cm))

#import data.template: render

#render(data)
