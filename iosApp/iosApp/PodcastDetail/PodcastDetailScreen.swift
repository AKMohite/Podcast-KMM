//
//  PodcastDetailScreen.swift
//  iosApp
//
//  Created by Ashish Mohite + on 11/06/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct PodcastDetailScreen: View {
    let podcastId: String
    
    @StateObject var viewModel = PodcastDetailViewModel()
    
    var body: some View {
        if #available(iOS 15.0, *) {
            ScrollView{
                
                if viewModel.isLoading { ProgressView() }
                
                if let safePodcast = viewModel.podcast  {
                    VStack{
                        
                        ZStack{
                            AsyncImage(url: URL(string: safePodcast.image)){image in
                                image.resizable().scaledToFill()
                            }placeholder: {
                                ProgressView()
                            }
                        }
                        .frame(maxWidth: .infinity, minHeight: 300, maxHeight: 300)
                        
                        VStack(alignment:.leading, spacing: 12){
                            Text(safePodcast.title)
                                .font(.title)
                                .fontWeight(.bold)
                                .fixedSize(horizontal: false, vertical: true)
                            
                            Button(action: {}){
                                HStack{
                                    Image(systemName: "play.fill")
                                        .foregroundColor(.black)
                                    
                                    Text("Start listening now")
                                        .foregroundColor(.black)
                                }
                            }
                            .frame(maxWidth: .infinity, maxHeight: 40)
                            .padding()
                            .background(.red)
                            .clipShape(RoundedRectangle(cornerRadius: 8))
                            
                            Text("By: \(safePodcast.publisher)".uppercased())
                            
                            Text(safePodcast.description_)
                                .font(.body)
                                .fixedSize(horizontal: false, vertical: true)
                            
                        }
                        .padding(20)
                        .background(.black)
                        
                    }
                    .frame(maxWidth: .infinity, maxHeight: .infinity)
                }
                
                
            }.task {
                await viewModel.loadPodcastAsync(id: podcastId)
            }
        } else {
            // Fallback on earlier versions
        }
    }
}

struct PodcastDetailScreen_Previews: PreviewProvider {
    static var previews: some View {
        PodcastDetailScreen(podcastId: "8758da9be6c8452884a8cab6373b007c")
    }
}
