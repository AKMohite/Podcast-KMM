//
//  PodcastGridItem.swift
//  iosApp
//
//  Created by Ashish Mohite + on 10/06/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct PodcastGridItem: View {
    let podcast: Podcast
    var body: some View {
        VStack(
            alignment: .leading,
            spacing: 8
        ){
            ZStack(){
                if #available(iOS 15.0, *) {
                    AsyncImage(url: URL(string: podcast.image)) { image in
                        image.resizable()
                    }placeholder: {
                        Color.gray
                    }
                } else {
                    // Fallback on earlier versions
                }
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity)
            .clipShape(RoundedRectangle(cornerRadius: 8))
            
            Text(podcast.title)
                .font(.title)
                .fontWeight(.bold)
                .lineLimit(1)
            Text(podcast.publisher)
                .font(.caption)
                .lineLimit(1)
        }.frame(maxWidth: .infinity, maxHeight: 260)
    }
}

struct PodcastGridItem_Previews: PreviewProvider {
    static var previews: some View {
        PodcastGridItem(podcast: samplePodcast)
    }
}
