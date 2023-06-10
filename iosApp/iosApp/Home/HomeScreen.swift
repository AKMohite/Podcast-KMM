import SwiftUI
import shared

struct HomeScreen: View {

    @StateObject var viewModel = HomeViewModel()
    
    let gridColumns: [GridItem] = Array(repeating: GridItem(.flexible(), spacing: 16), count: 2)
    
	var body: some View {
        if #available(iOS 16.0, *) {
            NavigationStack{
                ScrollView{
                    LazyVGrid(columns: gridColumns, spacing: 16) {
                        ForEach(viewModel.podcasts, id: \.id) { podcast in
                            NavigationLink(value: podcast) {
                                PodcastGridItem(podcast: podcast)
                                    .task {
                                        if podcast == viewModel.podcasts.last && !viewModel.isLoading && !viewModel.loadFinished {
                                            await viewModel.loadPodcasts()
                                        }
                                    }
                            }
                            .buttonStyle(PlainButtonStyle())
                        }
                        
                        if viewModel.isLoading {
                            Section(footer: ProgressView()){}
                        }
                    }
                    .padding(.horizontal, 12)
                    .navigationDestination(for: Podcast.self) { podast in
                        PodcastDetailScreen(podcast: podast)
                    }
                }
                .navigationTitle("Podcasts")
            }
            .task {
                await viewModel.loadPodcasts()
            }
        } else {
            // Fallback on earlier versions
        }
	}
}

struct HomeScreen_Previews: PreviewProvider {
	static var previews: some View {
		HomeScreen()
	}
}
